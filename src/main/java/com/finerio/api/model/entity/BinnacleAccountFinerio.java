package com.finerio.api.model.entity;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "binnacle_account_finerio")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BinnacleAccountFinerio {

    @Id
    private String id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Column(name = "date")
    private Date date;





}
