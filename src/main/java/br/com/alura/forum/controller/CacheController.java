package br.com.alura.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Controller
@RequestMapping("/admin/cache")
public class CacheController {

    @Autowired
    private CacheManager manager;

    @GetMapping("/clear")
    public String clearCache() {
        manager.getCacheNames()
                .forEach(name -> Objects.requireNonNull(manager.getCache(name)).clear());

        return "cache cleaned";
    }
}