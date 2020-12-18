package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.GiftCertificateTagDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.epam.esm.model.dao.impl.ColumnName.*;

@Repository
@Transactional
public class JdbcGiftCertificateDao implements GiftCertificateDao {

    private static final String SQL_SELECT_ALL_CERTIFICATES =
            "SELECT id, name, description, price, duration, create_date, last_update_date\n" +
                    "FROM gift_certificate";
    private static final String SQL_SELECT_CERTIFICATE = SQL_SELECT_ALL_CERTIFICATES + "\nWHERE id = ?";
    private static final String SQL_INSERT =
            "INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE =
            "UPDATE gift_certificate SET NAME = ?, description = ?, price = ?, duration = ?, last_update_date = ? \n" +
                    "WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM gift_certificate WHERE id = ?";

    private JdbcTemplate jdbcTemplate;
    private TagDao tagDao;
    private GiftCertificateTagDao giftCertificateTagDao;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Autowired
    public void setGiftCertificateTagDao(GiftCertificateTagDao giftCertificateTagDao) {
        this.giftCertificateTagDao = giftCertificateTagDao;
    }

    @Override
    @Transactional
    public Optional<GiftCertificate> findById(long id) {
        Optional<GiftCertificate> optional;
        try {
            GiftCertificate giftCertificate = jdbcTemplate.queryForObject(SQL_SELECT_CERTIFICATE,
                    new Object[]{id}, new GiftCertificateRowMapper());
            List<Tag> tags = giftCertificateTagDao.findAllTags(id);
            giftCertificate.setTags(tags);
            optional = Optional.of(giftCertificate);
        } catch (EmptyResultDataAccessException e) {
            optional = Optional.empty();
        }
        return optional;
    }

    @Override
    @Transactional
    public List<GiftCertificate> findAll() {
        List<GiftCertificate> certificates = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL_SELECT_ALL_CERTIFICATES);
        for (Map<String, Object> row : rows) {
            GiftCertificate giftCertificate = new GiftCertificate();
            giftCertificate.setId((Long) row.get(GIFT_CERTIFICATE_ID));
            giftCertificate.setName((String) row.get(GIFT_CERTIFICATE_NAME));
            giftCertificate.setDescription((String) row.get(GIFT_CERTIFICATE_DESCRIPTION));
            giftCertificate.setPrice((Integer) row.get(GIFT_CERTIFICATE_PRICE));
            giftCertificate.setDuration((Integer) row.get(GIFT_CERTIFICATE_DURATION));
            giftCertificate.setCreateDate((String) row.get(GIFT_CERTIFICATE_CREATE_DATE));
            giftCertificate.setLastUpdateDate((String) row.get(GIFT_CERTIFICATE_LAST_UPDATE_DATE));
            List<Tag> tags = giftCertificateTagDao.findAllTags(giftCertificate.getId());
            giftCertificate.setTags(tags);
            certificates.add(giftCertificate);
        }
        return certificates;
    }

    @Override
    @Transactional
    public GiftCertificate add(GiftCertificate entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getDescription());
            ps.setInt(3, entity.getPrice());
            ps.setInt(4, entity.getDuration());
            ps.setString(5, entity.getCreateDate());
            ps.setString(6, entity.getLastUpdateDate());
            return ps;
        }, keyHolder);
        long certificateId = keyHolder.getKey().longValue();
        GiftCertificate giftCertificate = findById(certificateId).get();
        for (Tag tag : entity.getTags()) {
            Tag addedTag = addTag(giftCertificate.getId(), tag);
            giftCertificate.addTag(addedTag);
        }
        return findById(keyHolder.getKey().longValue()).get();
    }

    @Override
    @Transactional
    public GiftCertificate update(GiftCertificate entity) {
        jdbcTemplate.update(SQL_UPDATE, entity.getName(), entity.getDescription(),
                entity.getPrice(), entity.getDuration(), entity.getLastUpdateDate(), entity.getId());
        giftCertificateTagDao.deleteAllTags(entity.getId());
        List<Tag> tags = entity.getTags();
        if (tags != null && !tags.isEmpty()) {
            for (Tag tag : entity.getTags()) {
                addTag(entity.getId(), tag);
            }
        }
        return findById(entity.getId()).get();
    }

    @Override
    @Transactional
    public void delete(long id) {
        giftCertificateTagDao.deleteAllTags(id);
        jdbcTemplate.update(SQL_DELETE, id);
    }

    private Tag addTag(long certificateId, Tag tag) {
        Optional<Tag> optionalTag = tagDao.findByName(tag.getName());
        Tag addedTag = optionalTag.orElseGet(() -> tagDao.add(tag));
        giftCertificateTagDao.add(certificateId, addedTag.getId());
        return addedTag;
    }

    private class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {

        @Override
        public GiftCertificate mapRow(ResultSet resultSet, int i) throws SQLException {
            GiftCertificate certificate = new GiftCertificate();
            certificate.setId(resultSet.getLong(GIFT_CERTIFICATE_ID));
            certificate.setName(resultSet.getString(GIFT_CERTIFICATE_NAME));
            certificate.setDescription(resultSet.getString(GIFT_CERTIFICATE_DESCRIPTION));
            certificate.setPrice(resultSet.getInt(GIFT_CERTIFICATE_PRICE));
            certificate.setDuration(resultSet.getInt(GIFT_CERTIFICATE_DURATION));
            certificate.setCreateDate(resultSet.getString(GIFT_CERTIFICATE_CREATE_DATE));
            certificate.setLastUpdateDate(resultSet.getString(GIFT_CERTIFICATE_LAST_UPDATE_DATE));
            return certificate;
        }
    }
}
