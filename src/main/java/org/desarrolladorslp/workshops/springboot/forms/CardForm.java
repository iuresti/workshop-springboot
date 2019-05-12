package org.desarrolladorslp.workshops.springboot.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.desarrolladorslp.workshops.springboot.validation.ValidationCreate;
import org.desarrolladorslp.workshops.springboot.validation.ValidationUpdate;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardForm implements Serializable {
    @NotNull(groups = ValidationUpdate.class)
    private Long id;
    @NotNull(groups = {ValidationCreate.class, ValidationUpdate.class})
    private String description;
    @NotNull(groups = ValidationCreate.class)
    private Long columnId;
}
