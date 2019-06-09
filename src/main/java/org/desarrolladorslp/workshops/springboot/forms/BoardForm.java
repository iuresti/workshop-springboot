package org.desarrolladorslp.workshops.springboot.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.desarrolladorslp.workshops.springboot.validation.ValidationCreate;
import org.desarrolladorslp.workshops.springboot.validation.ValidationUpdate;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiObject(name = "BoardForm", description = "Formulario de Board de usuario.")
public class BoardForm implements Serializable {
    @NotNull(groups = ValidationUpdate.class)
    @ApiObjectField(description = "Id de Board")
    private Long id;
    @NotBlank(groups = {ValidationCreate.class, ValidationUpdate.class})
    @ApiObjectField(description = "Nombre de Board", required = true)
    private String name;
    @ApiObjectField(description = "Id de User")
    private Long userId;
}
