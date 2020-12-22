package com.epam.esm.controller;

import com.epam.esm.controller.exception.GiftEntityNotFoundException;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/tags")
public class TagApiController {

    private TagService service;

    @Autowired
    public void setService(TagService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseBody
    public List<Tag> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Tag findById(@PathVariable long id) {
        return service.findById(id).orElseThrow(() ->
                new GiftEntityNotFoundException("Tag not found", ErrorCode.TAG_NOT_FOUND));
    }

    @PostMapping("/")
    @ResponseBody
    public Tag create(@RequestBody Tag tag) {
        return service.add(tag);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public Tag update(@RequestBody Tag tag, @PathVariable long id) {
        tag.setId(id);
        return service.update(tag).orElseThrow(() ->
                new GiftEntityNotFoundException("Tag not found", ErrorCode.TAG_NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable int id) {
        service.delete(id);
    }
}
