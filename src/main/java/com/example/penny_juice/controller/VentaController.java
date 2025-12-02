package com.example.penny_juice.controller;

import com.example.penny_juice.dto.PedidoCreateDTO;
import com.example.penny_juice.dto.PedidoDTO;
import com.example.penny_juice.model.Usuario;
import com.example.penny_juice.repository.UsuarioRepository;
import com.example.penny_juice.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/venta")
public class VentaController {

    private static final Logger logger = LoggerFactory.getLogger(VentaController.class);

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public String crearPedido(PedidoCreateDTO pedidoCreateDTO, org.springframework.ui.Model model, Principal principal) {
        logger.info("crearPedido invoked - principal={}, dto.IdUsuario={}", principal == null ? null : principal.getName(), pedidoCreateDTO == null ? null : pedidoCreateDTO.IdUsuario());

        PedidoCreateDTO dtoToUse = pedidoCreateDTO;

        // If the form did not send IdUsuario, try to obtain it from the authenticated principal
        if (dtoToUse == null || dtoToUse.IdUsuario() == null) {
            if (principal == null) {
                // Not authenticated: redirect to login
                return "redirect:/login";
            }
            String correo = principal.getName();
            Usuario usuario = usuarioRepository.findByCorreo(correo).orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));
            dtoToUse = new PedidoCreateDTO(usuario.getId(), dtoToUse == null ? null : dtoToUse.DireccionEnvio(), dtoToUse == null ? null : dtoToUse.detalles());
        }

        logger.info("crearPedido - creating pedido for userId={} detallesCount={}", dtoToUse.IdUsuario(), dtoToUse.detalles() == null ? 0 : dtoToUse.detalles().size());

        PedidoDTO resultado = pedidoService.crearPedido(dtoToUse);
        model.addAttribute("orderId", resultado.idPedido());
        model.addAttribute("total", resultado.total());
        return "payment-success";
    }

    @GetMapping
    public String listarPedidos(Model model){
        model.addAttribute("pedidos", pedidoService.listarTodasEntidades());
        return "ListaPedidos";
    }

    @GetMapping("/detalle/{id}")
    public String detallePedido(@PathVariable("id") Long id, Model model){
        var pedido = pedidoService.obtenerPorId(id);
        var detalles = pedidoService.obtenerDetallesPorPedidoId(id);
        model.addAttribute("pedido", pedido);
        model.addAttribute("detalles", detalles);
        return "detalle-pedido";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPedido(@PathVariable("id") Long id){
        pedidoService.eliminarPedido(id);
        return "redirect:/venta";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable("id") Long id, Model model){
        var pedido = pedidoService.obtenerPorId(id);
        model.addAttribute("pedido", pedido);
        return "editar-pedido";
    }

    @PostMapping("/editar")
    public String editarSubmit(@RequestParam("id") Long id, @RequestParam("estado") String estado){
        pedidoService.actualizarEstado(id, estado);
        return "redirect:/venta";
    }

}
