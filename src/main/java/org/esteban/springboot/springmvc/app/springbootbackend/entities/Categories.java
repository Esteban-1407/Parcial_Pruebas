package org.esteban.springboot.springmvc.app.springbootbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Categorias")
public class Categories {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(name = "nombre", nullable = false, unique = true)
        private String nombre;
        @Column(name = "descripcion", length = 500)
        private String descripcion;

        @Column(name = "fecha_creacion", updatable = false)
        private LocalDateTime fechaCreacion;

        @Column(name = "fecha_actualizacion")
        private LocalDateTime fechaActualizacion;

        @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @JsonIgnore
        private List<Product> productos;
}
