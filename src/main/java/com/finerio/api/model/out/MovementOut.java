package com.finerio.api.model.out;

import com.finerio.api.model.Movement;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovementOut {

    private List<Movement> data;
    private Integer size;

}
