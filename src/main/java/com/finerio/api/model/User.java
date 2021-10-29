package com.finerio.api.model;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    private String id;
    private boolean accountExpired;
    private boolean accountLocked;
    private String customerId;
    private Date dateCreated;
    private String email;
    private boolean enabled;
    private Date lastUpdated;
    private String name;
    private boolean notificationsEnabled;
    private boolean passwordExpired;
    private boolean signupCredentialEmailSent;
    private boolean signupCredentialPushSent;
    private boolean signupEmailSent;
    private String signupFrom;
    private boolean termsAndConditionsAccepted;
    private String type;
    private String finerioCode;
    private boolean hasNewTerms;
}
