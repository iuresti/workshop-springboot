package org.desarrolladorslp.workshops.springboot.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.desarrolladorslp.workshops.springboot.validation.ValidationCreate;
import org.desarrolladorslp.workshops.springboot.validation.ValidationUpdate;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiObject(name = "CardForm", description = "Formulario de Card de usuario.")
public class CardForm implements Serializable {
    @NotNull(groups = ValidationUpdate.class)
    @ApiObjectField(description = "Id de Card")
    private Long id;
    @NotNull(groups = {ValidationCreate.class, ValidationUpdate.class})
    @ApiObjectField(description = "Descripcion de Card", required = true)
    private String description;
    @NotNull(groups = ValidationCreate.class)
    @ApiObjectField(description = "Id de Column")
    private Long columnId;
}
