package com.finerio.api.model;


import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movement {

    private String id;
    private BigDecimal amount;
    private Double balance;
    private Date customDate;
    private String customDescription;
    private Date date;
    private String dateCreated;
    private boolean deleted;
    private String description;
    private boolean duplicated;
    private boolean hasConcepts;
    private boolean inResume;
    private Date lastUpdated;
    private String type;
    private Account account;
    private Category category;
}
