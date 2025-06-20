package org.esteban.springboot.springmvc.app.springbootbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoStockRequestDTO {


    private Long productoId;

    private Integer cantidad;

    private String motivo;
}
