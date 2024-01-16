package com.atm.inet.utils.specification;

import com.atm.inet.entity.computer.Computer;
import com.atm.inet.entity.constant.ECategory;
import com.atm.inet.entity.constant.EStatus;
import com.atm.inet.model.common.ComputerSearch;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ComputerSpecification {

    public static Specification<Computer> getSpecification(ComputerSearch computerSearch) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();


            if (computerSearch.getName() != null) {
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + computerSearch.getName().toLowerCase() + "%");
                predicateList.add(namePredicate);
            }

            if (computerSearch.getCode() != null) {
                Predicate codePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + computerSearch.getCode().toLowerCase() + "%");
                predicateList.add(codePredicate);
            }

            if (computerSearch.getProcessor() != null) {
                Predicate processorPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("processor")), "%" + computerSearch.getProcessor().toLowerCase() + "%");
                predicateList.add(processorPredicate);
            }

            if (computerSearch.getVga() != null) {
                Predicate vgaPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("vga")), "%" + computerSearch.getVga().toLowerCase() + "%");
                predicateList.add(vgaPredicate);
            }

            if (computerSearch.getCategory() != null) {
                Predicate typePredicate = criteriaBuilder.equal(root.get("type").get("category"), ECategory.valueOf(computerSearch.getCategory().toUpperCase()));
                predicateList.add(typePredicate);
            }
            if(computerSearch.getStatus() != null){
                Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), EStatus.valueOf(computerSearch.getStatus().toUpperCase()));
                predicateList.add(statusPredicate);
            }

            Predicate[] predicates = predicateList.toArray(new Predicate[0]);
            return criteriaBuilder.and(predicates);
        };
    }
}
