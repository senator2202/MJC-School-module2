package com.epam.esm.service.impl;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.SortType;

import java.util.Comparator;
import java.util.Optional;

class GiftCertificateComparatorProvider {
    private static final String DESC = "desc";
    private static final String ASC = "asc";

    static Optional<Comparator<GiftCertificate>> provide(String type, String direction) {
        Optional<Comparator<GiftCertificate>> optional;
        if (type != null) {
            try {
                SortType sortType = SortType.valueOf(type.toUpperCase());
                if (direction != null) {
                    if (direction.equals(DESC)) {
                        optional = Optional.of(sortType.getComparatorDesc());
                    } else if (direction.equals(ASC)) {
                        optional = Optional.of(sortType.getComparatorAsc());
                    } else {
                        optional = Optional.empty();
                    }
                } else {
                    optional = Optional.of(sortType.getComparatorAsc());
                }
            } catch (IllegalArgumentException e) {
                optional = Optional.empty();
            }
        } else {
            optional = Optional.empty();
        }
        return optional;
    }
}
