package com.atm.inet.entity.computer;


import com.atm.inet.entity.auditing.Auditable;
import com.atm.inet.entity.constant.ECategory;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "m_type")
public class Type extends Auditable<String> {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String id;

    @Enumerated(EnumType.STRING)
    private ECategory category;

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<TypePrice> typePrices;

    @OneToOne(targetEntity = ComputerImage.class, cascade = CascadeType.ALL)
    @JsonManagedReference
    private ComputerImage computerImage;

    public List<TypePrice> getTypePrices() {
        return Collections.unmodifiableList(typePrices);
    }

    public void addTypePrices(TypePrice typePrices) {
        this.typePrices.add(typePrices);
    }



}
