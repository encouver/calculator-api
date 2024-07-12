package com.finances.calculator.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

/**
 * @author Marcos Ramirez
 */
@MappedSuperclass
@Getter
@Setter
//@SQLDelete(sql = "UPDATE #{#entityName} SET deleted = true WHERE id=?")
//@SQLRestriction(value="deleted=false")
//@FilterDef(name = "deletedFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
//@Filter(name = "deletedFilter", condition = "deleted = :isDeleted")
@SoftDelete(strategy = SoftDeleteType.DELETED)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false, columnDefinition = "boolean DEFAULT false")
//    private Boolean deleted = Boolean.FALSE;


}
