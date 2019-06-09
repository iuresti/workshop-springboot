package org.desarrolladorslp.workshops.springboot.forms;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.desarrolladorslp.workshops.springboot.validation.ValidationCreate;
import org.desarrolladorslp.workshops.springboot.validation.ValidationUpdate;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiObject(name = "ColumnForm", description = "Formulario de Column de usuario.")
public class ColumnForm implements Serializable {
    @NotNull(groups = ValidationUpdate.class)
    @ApiObjectField(description = "Id de Column")
    private Long id;
    @NotNull(groups = {ValidationCreate.class, ValidationUpdate.class})
    @ApiObjectField(description = "Nombre de Column", required = true)
    private String name;
    @NotNull(groups = ValidationCreate.class)
    @ApiObjectField(description = "Id de Board")
    private Long board;
}
