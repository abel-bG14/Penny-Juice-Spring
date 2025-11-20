package com.example.penny_juice.controller;

import com.example.penny_juice.dto.VentaView;
import com.example.penny_juice.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping
public class VentaController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/ventas")
    public String verVentas(Model model){
        List<VentaView> ventas = pedidoService.listarTodas();
        model.addAttribute("ventas", ventas);
        return "ventas";
    }

    @GetMapping("/api/ventas")
    @ResponseBody
    public List<VentaView> apiListarVentas(){
        return pedidoService.listarTodas();
    }
}
