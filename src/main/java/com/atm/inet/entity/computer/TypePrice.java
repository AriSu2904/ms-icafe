package com.atm.inet.entity.computer;

import com.atm.inet.entity.auditing.Auditable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "m_type_price")
public class TypePrice extends Auditable<String> {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String id;

    @ManyToOne
    @JoinColumn(name = "type_id")
    @JsonBackReference
    private Type type;

    @Column(name = "price", columnDefinition = "bigint check(price > 0)")
    private Long price;

    @Column(name = "is_active")
    private Boolean isActive;

}

