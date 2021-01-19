package com.epam.esm.model.dao.impl;

import com.epam.esm.data_provider.StaticDataProvider;
import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.entity.GiftCertificate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class JdbcGiftCertificateDaoTest {

    private GiftCertificateDao giftCertificateDao;
    private EmbeddedDatabase embeddedDatabase;

    static Stream<Arguments> findAllArgs() {
        return Stream.of(
                Arguments.of("Тату", null, null, null, null, 2),
                Arguments.of("Театр", null, null, null, null, 1),
                Arguments.of(null, "Бесплатн", null, null, null, 2),
                Arguments.of(null, null, "Активность", null, null, 3),
                Arguments.of(null, null, null, null, null, 10)
        );
    }

    @BeforeEach
    void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        giftCertificateDao = new JdbcGiftCertificateDao();
        ((JdbcGiftCertificateDao) giftCertificateDao).setJdbcTemplate(jdbcTemplate);
        JdbcGiftCertificateTagDao giftCertificateTagDao = new JdbcGiftCertificateTagDao();
        giftCertificateTagDao.setJdbcTemplate(jdbcTemplate);
        ((JdbcGiftCertificateDao) giftCertificateDao).setGiftCertificateTagDao(giftCertificateTagDao);
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Test
    void findByIdExist() {
        Optional<GiftCertificate> optional = giftCertificateDao.findById(1L);
        assertTrue(optional.isPresent() && optional.get().getName().equals("Сауна Тритон"));
    }

    @Test
    void findByIdNotExist() {
        Optional<GiftCertificate> optional = giftCertificateDao.findById(199L);
        assertFalse(optional.isPresent());
    }

    @Test
    void add() {
        giftCertificateDao.add(StaticDataProvider.ADDING_CERTIFICATE);
        List<GiftCertificate> allCertificates = giftCertificateDao.findAll(null, null, null, null, null);
        assertEquals(11, allCertificates.size());
    }

    @Test
    void update() {
        GiftCertificate giftCertificate = giftCertificateDao.findById(1L).get();
        giftCertificate.setPrice(BigDecimal.valueOf(155));
        GiftCertificate updated = giftCertificateDao.update(giftCertificate);
        assertEquals(155, updated.getPrice().intValue());
    }

    @Test
    void delete() {
        giftCertificateDao.delete(1L);
        Optional<GiftCertificate> optional = giftCertificateDao.findById(1L);
        assertFalse(optional.isPresent());
    }

    @ParameterizedTest
    @MethodSource("findAllArgs")
    void testFindAll(String name, String description, String tagName, String sortType, String direction, int size) {
        List<GiftCertificate> actual = giftCertificateDao.findAll(name, description, tagName, sortType, direction);
        assertEquals(size, actual.size());
    }
}