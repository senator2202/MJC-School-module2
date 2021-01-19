package com.epam.esm.service.impl;

import com.epam.esm.data_provider.StaticDataProvider;
import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.entity.GiftCertificate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class GiftCertificateServiceImplTest {

    @InjectMocks
    private final GiftCertificateServiceImpl giftCertificateService = new GiftCertificateServiceImpl();

    @Mock
    private GiftCertificateDao giftCertificateDao;

    @Mock
    private TransactionTemplate transactionTemplate;

    @BeforeEach
    private void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findById() {
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.ofNullable(StaticDataProvider.GIFT_CERTIFICATE));
        Optional<GiftCertificate> actual = giftCertificateService.findById(1L);
        Optional<GiftCertificate> expected = Optional.of(StaticDataProvider.GIFT_CERTIFICATE);
        assertEquals(actual, expected);
    }

    @Test
    void add() {
        when(transactionTemplate.execute(any())).thenReturn(StaticDataProvider.GIFT_CERTIFICATE);
        GiftCertificate actual = giftCertificateService.add(StaticDataProvider.ADDING_CERTIFICATE);
        GiftCertificate expected = StaticDataProvider.GIFT_CERTIFICATE;
        assertEquals(actual, expected);
    }

    @Test
    void updateIfExist() {
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.of(StaticDataProvider.GIFT_CERTIFICATE));
        when(transactionTemplate.execute(any())).thenReturn(StaticDataProvider.UPDATED_GIFT_CERTIFICATE);
        Optional<GiftCertificate> actual = giftCertificateService.update(StaticDataProvider.UPDATED_GIFT_CERTIFICATE);
        Optional<GiftCertificate> expected = Optional.of(StaticDataProvider.UPDATED_GIFT_CERTIFICATE);
        assertEquals(actual, expected);
    }

    @Test
    void updateIfNotExist() {
        when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.empty());
        Optional<GiftCertificate> actual = giftCertificateService.update(StaticDataProvider.UPDATED_GIFT_CERTIFICATE);
        Optional<GiftCertificate> expected = Optional.empty();
        assertEquals(actual, expected);
    }

    @Test
    void delete() {
        when(transactionTemplate.execute(any())).thenReturn(true);
        boolean actual = giftCertificateService.delete(1L);
        assertTrue(actual);

    }
}