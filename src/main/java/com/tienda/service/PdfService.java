package com.tienda.service;

import com.tienda.model.Item;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;

@Service
public class PdfService {

    public byte[] generatePdf(List<Item> items) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream content = new PDPageContentStream(document, page);
            content.setFont(PDType1Font.HELVETICA_BOLD, 14);
            content.beginText();
            content.newLineAtOffset(100, 750);
            content.showText("Listado de Productos");
            content.endText();

            float yPosition = 720; // Posición inicial en la página

            // Cabecera de la tabla
            content.setFont(PDType1Font.HELVETICA_BOLD, 10);
            content.beginText();
            content.newLineAtOffset(50, yPosition);
            content.showText("Imagen");
            content.newLineAtOffset(100, 0);
            content.showText("Nombre");
            content.newLineAtOffset(180, 0);
            content.showText("Descripción");
            content.newLineAtOffset(200, 0);
            content.showText("Precio");
            content.newLineAtOffset(100, 0);
            content.showText("Categoría");
            content.endText();

            yPosition -= 20;

            // Dibujar los datos de los productos
            for (Item item : items) {
                if (yPosition < 100) { // Crear nueva página si no hay espacio
                    content.close();
                    page = new PDPage();
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    yPosition = 750;
                }

                // Cargar imagen del producto
                PDImageXObject image = null;
                try {
                    BufferedImage bufferedImage = ImageIO.read(new URL(item.getImage()));
                    image = PDImageXObject.createFromByteArray(document,
                            bufferedImageToBytes(bufferedImage), item.getTitle());
                } catch (Exception e) {
                    System.out.println("No se pudo cargar la imagen: " + item.getImage());
                }

                // Dibujar la imagen si se cargó correctamente
                if (image != null) {
                    content.drawImage(image, 50, yPosition - 10, 40, 40);
                }

                // Dibujar el texto de los productos
                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 10);
                content.newLineAtOffset(150, yPosition);
                content.showText(item.getTitle());
                content.newLineAtOffset(180, 0);
                content.showText(item.getDescription());
                content.newLineAtOffset(200, 0);
                content.showText("$" + item.getPrice());
                content.newLineAtOffset(100, 0);
                content.showText(item.getCategory());
                content.endText();

                yPosition -= 50; // Espaciado entre filas
            }

            content.close();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para convertir una imagen BufferedImage a byte[]
    private byte[] bufferedImageToBytes(BufferedImage bufferedImage) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "jpg", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        }
    }
}
