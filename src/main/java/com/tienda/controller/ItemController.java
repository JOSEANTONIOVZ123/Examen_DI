package com.tienda.controller;

import com.tienda.model.Item;
import com.tienda.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    // 🔹 Página principal (Listado de productos)
    @GetMapping("/")
    public String showItems(Model model) {
        model.addAttribute("items", itemService.getAllItems());
        return "index";
    }

    // 🔹 Historia de Usuario B: Obtener detalles del producto
    @GetMapping("/detalle/{id}")
    public String getItemDetails(@PathVariable String id, Model model) {
        Optional<Item> item = itemService.getItemById(id);
        if (item.isPresent()) {
            model.addAttribute("item", item.get());
            return "detalle"; // Renderiza la página detalle.html
        } else {
            model.addAttribute("mensaje", "El producto con ID " + id + " no existe.");
            return "error"; // Muestra una página de error en vez de WhiteLabel Page
        }
    }
}
