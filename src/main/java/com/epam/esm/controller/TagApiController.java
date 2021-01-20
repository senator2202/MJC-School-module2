package com.epam.esm.controller;

import com.epam.esm.controller.exception.GiftEntityNotFoundException;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Tag api controller.
 */
@RestController
@RequestMapping("api/tags")
public class TagApiController {

    private TagService service;

    @Autowired
    public void setService(TagService service) {
        this.service = service;
    }

    /**
     * End point for findAllTags request.
     */
    @GetMapping
    public List<Tag> findAll() {
        return service.findAll();
    }

    /**
     * End point for finding tag by id request.
     */
    @GetMapping("/{id}")
    public Tag findById(@PathVariable long id) {
        return service.findById(id).orElseThrow(() ->
                new GiftEntityNotFoundException("Tag not found", ErrorCode.TAG_NOT_FOUND));
    }

    /**
     * End point for adding new tag request.
     */
    @PostMapping
    public Tag create(@RequestBody Tag tag) {
        return service.add(tag);
    }

    /**
     * End point for updating tag request.
     */
    @PutMapping("/{id}")
    public Tag update(@RequestBody Tag tag, @PathVariable long id) {
        tag.setId(id);
        return service.update(tag).orElseThrow(() ->
                new GiftEntityNotFoundException("Tag not found", ErrorCode.TAG_NOT_FOUND));
    }

    /**
     * End point for deleting tag request.
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable int id) {
        return service.delete(id);
    }
}
