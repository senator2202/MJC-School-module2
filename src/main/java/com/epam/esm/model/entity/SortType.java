package com.epam.esm.model.entity;

import java.util.Comparator;

public enum SortType {
    NAME((c1, c2) -> c1.getName().compareTo(c2.getName())),
    CREATE_DATE((c1, c2) -> c1.getCreateDate().compareTo(c2.getCreateDate())),
    LAST_UPDATE_DATE((c1, c2) -> c1.getLastUpdateDate().compareTo(c2.getLastUpdateDate())),
    PRICE((c1, c2) -> c1.getPrice() - c2.getPrice()),
    DURATION((c1, c2) -> c1.getDuration() - c2.getDuration());

    private Comparator<GiftCertificate> comparator;

    SortType(Comparator<GiftCertificate> comparator) {
        this.comparator = comparator;
    }

    public Comparator<GiftCertificate> getComparatorAsc() {
        return comparator;
    }

    public Comparator<GiftCertificate> getComparatorDesc() {
        return comparator.reversed();
    }
}
