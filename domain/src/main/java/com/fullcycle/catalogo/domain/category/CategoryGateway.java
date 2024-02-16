package com.fullcycle.catalogo.domain.category;

import com.fullcycle.catalogo.domain.pagination.Pagination;

import java.util.List;
import java.util.Set;

public interface CategoryGateway {

    Category save(Category aCategory);

    void deleteById(String anId);

    Pagination<Category> findAll(CategorySearchQuery aQuery);

    List<Category> findAllById(Set<String> ids);

}
