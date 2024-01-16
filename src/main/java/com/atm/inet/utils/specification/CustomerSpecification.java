package com.atm.inet.utils.specification;

import com.atm.inet.entity.Customer;
import com.atm.inet.model.common.CustomerSearch;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {
    public static Specification<Customer> getSpecification(CustomerSearch customerSearch){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if(customerSearch.getCustomerFirstName() != null){
                Predicate firstName = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + customerSearch.getCustomerFirstName().toLowerCase() + "%");
                predicateList.add(firstName);
            }
            if(customerSearch.getCustomerLastName() != null){
                Predicate lastName = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + customerSearch.getCustomerLastName().toLowerCase() + "%");
                predicateList.add(lastName);
            }
            Predicate[] predicates = predicateList.toArray(new Predicate[0]);
            return criteriaBuilder.and(predicates);
        };
    }

}
