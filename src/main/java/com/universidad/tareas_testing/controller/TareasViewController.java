package com.universidad.tareas_testing.controller;

import com.universidad.tareas_testing.entity.Tarea;
import com.universidad.tareas_testing.service.TareaService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TareasViewController {

    private final TareaService service;

    public TareasViewController(TareaService service) {
        this.service = service;
    }

    @GetMapping(value = "/tareas", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String listar() {
        StringBuilder html = new StringBuilder();
        html.append("""
                <!doctype html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <title>Tareas</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 32px; color: #202124; }
                        header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 24px; }
                        a, button { background: #155eef; color: white; border: 0; padding: 10px 14px; text-decoration: none; border-radius: 6px; cursor: pointer; }
                        .tarea-item { border: 1px solid #d0d7de; border-radius: 6px; padding: 12px; margin-bottom: 10px; }
                        .estado { color: #57606a; font-size: 14px; }
                    </style>
                </head>
                <body>
                    <header>
                        <h1>Listado de Tareas</h1>
                        <a id="btn-nueva" href="/tareas/nueva">Nueva tarea</a>
                    </header>
                    <main id="lista-tareas">
                """);
        for (Tarea tarea : service.listar()) {
            html.append("<article class=\"tarea-item\"><strong>")
                    .append(escapar(tarea.getTitulo()))
                    .append("</strong><p>")
                    .append(escapar(tarea.getDescripcion() == null ? "" : tarea.getDescripcion()))
                    .append("</p><span class=\"estado\">")
                    .append(tarea.isCompletada() ? "Completada" : "Pendiente")
                    .append("</span></article>");
        }
        html.append("""
                    </main>
                </body>
                </html>
                """);
        return html.toString();
    }

    @GetMapping(value = "/tareas/nueva", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String nueva() {
        return """
                <!doctype html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <title>Nueva Tarea</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 32px; color: #202124; }
                        form { max-width: 520px; display: grid; gap: 12px; }
                        label { display: grid; gap: 6px; font-weight: 700; }
                        input, textarea { border: 1px solid #d0d7de; border-radius: 6px; padding: 10px; font: inherit; }
                        button { background: #155eef; color: white; border: 0; padding: 10px 14px; border-radius: 6px; cursor: pointer; width: fit-content; }
                    </style>
                </head>
                <body>
                    <h1>Nueva Tarea</h1>
                    <form method="post" action="/tareas">
                        <label>Titulo <input id="titulo" name="titulo" required></label>
                        <label>Descripcion <textarea id="descripcion" name="descripcion"></textarea></label>
                        <button id="btn-guardar" type="submit">Guardar</button>
                    </form>
                </body>
                </html>
                """;
    }

    @PostMapping("/tareas")
    public String crear(String titulo, String descripcion) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(titulo);
        tarea.setDescripcion(descripcion);
        service.crear(tarea);
        return "redirect:/tareas";
    }

    private String escapar(String valor) {
        return valor.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
