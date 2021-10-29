package com.finerio.api.model.out;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginOut {

    private UserOut userFinero;
    private String token;
}
