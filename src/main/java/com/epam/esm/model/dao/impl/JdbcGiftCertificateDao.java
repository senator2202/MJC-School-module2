package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.GiftCertificateTagDao;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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
public class JdbcGiftCertificateDao implements GiftCertificateDao {

    private static final String SQL_SELECT_ALL_CERTIFICATES =
            "SELECT id, name, description, price, duration, create_date, last_update_date\n" +
                    "FROM gift_certificate ";
    private static final String SQL_SELECT_CERTIFICATE = SQL_SELECT_ALL_CERTIFICATES + "\nWHERE id = ?";
    private static final String SQL_INSERT =
            "INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE =
            "UPDATE gift_certificate SET NAME = ?, description = ?, price = ?, duration = ?, last_update_date = ? \n" +
                    "WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM gift_certificate WHERE id = ?";
    private static final String SQL_FIND_ALL_JOIN_TABLES =
            "SELECT DISTINCT g.id, g.name, g.description, g.price, g.duration, g.create_date, g.last_update_date\n" +
                    "FROM gift_certificate AS g JOIN certificate_tag as ct ON g.id=ct.gift_certificate_id " +
                    "JOIN tag AS t ON ct.tag_id=t.id \n";
    private static final String PERCENT = "%";
    private static final String WHERE = "WHERE ";
    private static final String NAME_LIKE = "g.name LIKE ? ";
    private static final String DESCRIPTION_LIKE = "description LIKE ? ";
    private static final String TAG_NAME_EQUALS = "t.name = ? ";
    private static final String AND = "AND ";
    private static final String ORDER_BY = "ORDER BY ";
    private static final String SPACE = " ";

    private JdbcTemplate jdbcTemplate;
    private GiftCertificateTagDao giftCertificateTagDao;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setGiftCertificateTagDao(GiftCertificateTagDao giftCertificateTagDao) {
        this.giftCertificateTagDao = giftCertificateTagDao;
    }

    @Override
    public Optional<GiftCertificate> findById(long id) {
        Optional<GiftCertificate> optional;
        try {
            GiftCertificate giftCertificate = jdbcTemplate.queryForObject(SQL_SELECT_CERTIFICATE,
                    new Object[]{id}, new GiftCertificateRowMapper());
            List<Tag> tags = giftCertificateTagDao.findAllTagsForCertificate(id);
            giftCertificate.setTags(tags);
            optional = Optional.of(giftCertificate);
        } catch (EmptyResultDataAccessException e) {
            optional = Optional.empty();
        }
        return optional;
    }

    @Override
    public GiftCertificate add(GiftCertificate entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getDescription());
            ps.setBigDecimal(3, entity.getPrice());
            ps.setInt(4, entity.getDuration() != null ? entity.getDuration() : 0);
            ps.setString(5, entity.getCreateDate());
            ps.setString(6, entity.getLastUpdateDate());
            return ps;
        }, keyHolder);
        return findById(keyHolder.getKey().longValue()).get();
    }

    @Override
    public GiftCertificate update(GiftCertificate entity) {
        jdbcTemplate.update(SQL_UPDATE, entity.getName(), entity.getDescription(),
                entity.getPrice(), entity.getDuration(), entity.getLastUpdateDate(), entity.getId());
        return findById(entity.getId()).get();
    }

    @Override
    public boolean delete(long id) {
        return jdbcTemplate.update(SQL_DELETE, id) > 0;
    }

    @Override
    public List<GiftCertificate> findAll(String name,
                                         String description,
                                         String tagName,
                                         String sortType,
                                         String direction) {
        StringBuilder sb;
        List<String> parameterList = new ArrayList<>();
        if (name != null || description != null || tagName != null) {
            sb = new StringBuilder(SQL_FIND_ALL_JOIN_TABLES);
            sb.append(WHERE);
        } else {
            sb = new StringBuilder(SQL_SELECT_ALL_CERTIFICATES);
        }
        if (name != null) {
            sb.append(NAME_LIKE);
            parameterList.add(PERCENT + name + PERCENT);
        }
        if (description != null) {
            sb.append(parameterList.isEmpty() ? DESCRIPTION_LIKE : AND + DESCRIPTION_LIKE);
            parameterList.add(PERCENT + description + PERCENT);
        }
        if (tagName != null) {
            sb.append(parameterList.isEmpty() ? TAG_NAME_EQUALS : AND + TAG_NAME_EQUALS);
            parameterList.add(tagName);
        }
        if (sortType != null) {
            sb.append(ORDER_BY).append(sortType);
        }
        if (direction != null) {
            sb.append(SPACE).append(direction);
        }
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sb.toString(), parameterList.toArray());
        return getGiftCertificates(rows);
    }

    private List<GiftCertificate> getGiftCertificates(List<Map<String, Object>> rows) {
        List<GiftCertificate> certificates = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            GiftCertificate giftCertificate = new GiftCertificate();
            giftCertificate.setId((Long) row.get(GIFT_CERTIFICATE_ID));
            giftCertificate.setName((String) row.get(GIFT_CERTIFICATE_NAME));
            giftCertificate.setDescription((String) row.get(GIFT_CERTIFICATE_DESCRIPTION));
            giftCertificate.setPrice((BigDecimal) row.get(GIFT_CERTIFICATE_PRICE));
            giftCertificate.setDuration((Integer) row.get(GIFT_CERTIFICATE_DURATION));
            giftCertificate.setCreateDate((String) row.get(GIFT_CERTIFICATE_CREATE_DATE));
            giftCertificate.setLastUpdateDate((String) row.get(GIFT_CERTIFICATE_LAST_UPDATE_DATE));
            List<Tag> tags = giftCertificateTagDao.findAllTagsForCertificate(giftCertificate.getId());
            giftCertificate.setTags(tags);
            certificates.add(giftCertificate);
        }
        return certificates;
    }


    private class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {

        @Override
        public GiftCertificate mapRow(ResultSet resultSet, int i) throws SQLException {
            GiftCertificate certificate = new GiftCertificate();
            certificate.setId(resultSet.getLong(GIFT_CERTIFICATE_ID));
            certificate.setName(resultSet.getString(GIFT_CERTIFICATE_NAME));
            certificate.setDescription(resultSet.getString(GIFT_CERTIFICATE_DESCRIPTION));
            certificate.setPrice(resultSet.getBigDecimal(GIFT_CERTIFICATE_PRICE));
            certificate.setDuration(resultSet.getInt(GIFT_CERTIFICATE_DURATION));
            certificate.setCreateDate(resultSet.getString(GIFT_CERTIFICATE_CREATE_DATE));
            certificate.setLastUpdateDate(resultSet.getString(GIFT_CERTIFICATE_LAST_UPDATE_DATE));
            return certificate;
        }
    }
}
