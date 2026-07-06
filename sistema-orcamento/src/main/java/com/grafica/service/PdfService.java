package com.grafica.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.grafica.model.Orcamento;
import com.grafica.model.ItemOrcamento;
import com.grafica.model.ConfiguracaoPdf;
import com.grafica.dao.OrcamentoDAO;
import com.grafica.dao.ItemOrcamentoDAO;
import com.grafica.dao.ConfiguracaoPdfDAO;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfService {

    public static String gerarOrcamentoPdfPorId(Integer idOrcamento, String diretorioDestino) {
        OrcamentoDAO orcamentoDAO = new OrcamentoDAO();
        Orcamento orcamento = orcamentoDAO.buscarPorId(idOrcamento);
        
        if (orcamento == null) {
            throw new RuntimeException("Orçamento não encontrado: " + idOrcamento);
        }
        
        ConfiguracaoPdf config = new ConfiguracaoPdfDAO().obter();
        String caminhoLogo = config != null ? config.getLogoPath() : null;
        
        return gerarOrcamentoPdf(orcamento, caminhoLogo, diretorioDestino);
    }

    public static String gerarOrcamentoPdf(Orcamento orcamento, String caminhoLogo, String diretorioDestino) {
        try {
            OrcamentoDAO orcamentoDAO = new OrcamentoDAO();
            ItemOrcamentoDAO itemDAO = new ItemOrcamentoDAO();

            List<ItemOrcamento> itens = itemDAO.listarPorOrcamento(orcamento.getId());
            
            System.out.println("=== DEBUG PDF ===");
            System.out.println("ID do Orçamento: " + orcamento.getId());
            System.out.println("Quantidade de itens encontrados: " + itens.size());
            for (int i = 0; i < itens.size(); i++) {
                ItemOrcamento item = itens.get(i);
                System.out.println("Item " + (i+1) + ": " + 
                    (item.getProduto() != null ? item.getProduto().getNome() : "N/A") + " | " +
                    (item.getMaterial() != null ? item.getMaterial().getNome() : "N/A") + " | " +
                    "Qtd: " + item.getQuantidade());
            }
            System.out.println("=== FIM DEBUG ===");

            if (itens.isEmpty()) {
                throw new RuntimeException("Orçamento sem itens. Adicione itens antes de gerar o PDF.");
            }

            if (diretorioDestino == null || diretorioDestino.isEmpty()) {
                diretorioDestino = System.getProperty("user.home") + "/Orcamentos/";
            }
            
            if (!diretorioDestino.endsWith("/")) {
                diretorioDestino += "/";
            }

            Document document = new Document(PageSize.A4, 36, 36, 54, 36);
            String nomeArquivo = diretorioDestino + "Orcamento_" + orcamento.getId() + ".pdf";
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nomeArquivo));

            document.open();

            adicionarCabecalho(document, orcamento, caminhoLogo);
            adicionarDadosCliente(document, orcamento);
            adicionarTabelaItens(document, itens);
            adicionarTotais(document, orcamento, itens);
            adicionarRodape(document, orcamento.getDataValidade());

            document.close();
            return nomeArquivo;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF: " + e.getMessage(), e);
        }
    }

    public static String gerarOrcamentoPdf(Orcamento orcamento, String caminhoLogo) {
        return gerarOrcamentoPdf(orcamento, caminhoLogo, null);
    }

    private static void adicionarCabecalho(Document document, Orcamento orcamento, String caminhoLogo) throws DocumentException {
        ConfiguracaoPdf config = new ConfiguracaoPdfDAO().obter();
        
        String nomeEmpresa = config != null && config.getNomeEmpresa() != null ? config.getNomeEmpresa() : "GRAFICA";
        String cnpj = config != null && config.getCnpj() != null ? config.getCnpj() : "";
        String endereco = config != null && config.getEndereco() != null ? config.getEndereco() : "";
        String telefone = config != null && config.getTelefone() != null ? config.getTelefone() : "";
        String email = config != null && config.getEmail() != null ? config.getEmail() : "";
        
        if (caminhoLogo == null || caminhoLogo.isEmpty()) {
            caminhoLogo = config != null ? config.getLogoPath() : null;
        }

        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidths(new int[]{1, 2});
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingBefore(10f);

        if (caminhoLogo != null && !caminhoLogo.isEmpty()) {
            try {
                Image logo = Image.getInstance(caminhoLogo);
                logo.scaleToFit(120, 80);
                PdfPCell logoCell = new PdfPCell(logo, false);
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headerTable.addCell(logoCell);
            } catch (Exception e) {
                PdfPCell emptyCell = new PdfPCell(new Phrase(""));
                emptyCell.setBorder(Rectangle.NO_BORDER);
                headerTable.addCell(emptyCell);
            }
        } else {
            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
            emptyCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(emptyCell);
        }

        PdfPCell infoCell = new PdfPCell();
        infoCell.setBorder(Rectangle.NO_BORDER);
        infoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);

        Paragraph titulo = new Paragraph(nomeEmpresa, titleFont);
        titulo.setSpacingAfter(3f);
        infoCell.addElement(titulo);
        
        if (!cnpj.isEmpty()) {
            infoCell.addElement(new Paragraph("CNPJ: " + cnpj, normalFont));
        }
        if (!endereco.isEmpty()) {
            infoCell.addElement(new Paragraph(endereco, normalFont));
        }
        if (!telefone.isEmpty() || !email.isEmpty()) {
            String contato = "";
            if (!telefone.isEmpty()) contato += "Tel: " + telefone;
            if (!email.isEmpty()) {
                if (!telefone.isEmpty()) contato += " | ";
                contato += "Email: " + email;
            }
            infoCell.addElement(new Paragraph(contato, normalFont));
        }

        infoCell.addElement(Chunk.NEWLINE);
        infoCell.addElement(new Paragraph("ORÇAMENTO Nº " + orcamento.getId(), boldFont));
        infoCell.addElement(new Paragraph("Data: " + orcamento.getDataEmissao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), normalFont));
        infoCell.addElement(new Paragraph("Válido até: " + orcamento.getDataValidade().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), normalFont));

        headerTable.addCell(infoCell);
        document.add(headerTable);
        document.add(Chunk.NEWLINE);
    }

    private static void adicionarDadosCliente(Document document, Orcamento orcamento) throws DocumentException {
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        PdfPTable clienteTable = new PdfPTable(2);
        clienteTable.setWidths(new int[]{1, 3});
        clienteTable.setWidthPercentage(100);
        clienteTable.setSpacingBefore(10f);

        clienteTable.addCell(createInfoCell("Cliente:", labelFont));
        clienteTable.addCell(createInfoCell("Cliente ID: " + orcamento.getIdCliente(), valueFont));

        document.add(clienteTable);
        document.add(Chunk.NEWLINE);
    }

    private static PdfPCell createInfoCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingBottom(5f);
        return cell;
    }

    private static void adicionarTabelaItens(Document document, List<ItemOrcamento> itens) throws DocumentException {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);

        System.out.println("Criando tabela PDF com " + itens.size() + " itens...");
        
        PdfPTable table = new PdfPTable(6);
        table.setWidths(new int[]{1, 2, 1, 1, 1, 1});
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        table.addCell(createHeaderCell("Produto", headerFont));
        table.addCell(createHeaderCell("Material", headerFont));
        table.addCell(createHeaderCell("Dimensões", headerFont));
        table.addCell(createHeaderCell("Qtd", headerFont));
        table.addCell(createHeaderCell("Vl. Bruto", headerFont));
        table.addCell(createHeaderCell("Vl. Final", headerFont));

        int count = 0;
        for (ItemOrcamento item : itens) {
            count++;
            String nomeProduto = item.getProduto() != null ? item.getProduto().getNome() : "N/A";
            String nomeMaterial = item.getMaterial() != null ? item.getMaterial().getNome() : "N/A";
            
            System.out.println("Adicionando item " + count + " na tabela: " + nomeProduto);
            
            table.addCell(createCell(nomeProduto, cellFont));
            table.addCell(createCell(nomeMaterial, cellFont));
            table.addCell(createCell(item.getLarguraMm() + " x " + item.getAlturaMm() + " mm", cellFont));
            table.addCell(createCell(String.valueOf(item.getQuantidade()), cellFont));
            table.addCell(createCell(formatarMoeda(item.getValorBrutoItem()), cellFont));
            table.addCell(createCell(formatarMoeda(item.getValorFinalItem()), cellFont));
        }
        
        System.out.println("Tabela criada com " + count + " itens adicionados.");

        document.add(table);
    }

    private static void adicionarTotais(Document document, Orcamento orcamento, List<ItemOrcamento> itens) throws DocumentException {
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidths(new int[]{3, 1});
        totalTable.setWidthPercentage(100);
        totalTable.setSpacingBefore(20f);
        totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        totalTable.addCell(createInfoCell("", labelFont));
        totalTable.addCell(createInfoCell("Total Bruto:", labelFont));

        totalTable.addCell(createInfoCell("", labelFont));
        totalTable.addCell(createInfoCell(formatarMoeda(orcamento.getValorBruto()), valueFont));

        if (orcamento.getDescontoProgressivo() != null && orcamento.getDescontoProgressivo().compareTo(BigDecimal.ZERO) > 0) {
            totalTable.addCell(createInfoCell("", labelFont));
            totalTable.addCell(createInfoCell("Desconto (" + orcamento.getDescontoProgressivo() + "%):", labelFont));
        }

        totalTable.addCell(createInfoCell("", labelFont));
        totalTable.addCell(createInfoCell("Margem (" + orcamento.getMargemLucroPercentual() + "%):", labelFont));

        PdfPCell finalCell = new PdfPCell(new Phrase("TOTAL FINAL", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        finalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        finalCell.setBorder(Rectangle.NO_BORDER);
        finalCell.setPaddingTop(10f);
        totalTable.addCell(finalCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(formatarMoeda(orcamento.getValorFinal()), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPaddingTop(10f);
        totalTable.addCell(valueCell);

        document.add(totalTable);
    }

    private static void adicionarRodape(Document document, LocalDate dataValidade) throws DocumentException {
        ConfiguracaoPdf config = new ConfiguracaoPdfDAO().obter();
        String rodapePersonalizado = config != null && config.getRodape() != null ? config.getRodape() : null;
        
        Font rodapeFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8);
        Paragraph rodape;
        
        if (rodapePersonalizado != null && !rodapePersonalizado.isEmpty()) {
            rodape = new Paragraph(rodapePersonalizado + " (Válido até " + dataValidade.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")", rodapeFont);
        } else {
            rodape = new Paragraph(
                "Este orçamento tem validade até " + dataValidade.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + 
                ". Valores sujeitos a alteração sem aviso prévio.", 
                rodapeFont
            );
        }
        
        rodape.setAlignment(Element.ALIGN_CENTER);
        rodape.setSpacingBefore(30f);
        document.add(rodape);
    }

    private static PdfPCell createHeaderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
        cell.setPadding(5);
        return cell;
    }

    private static PdfPCell createCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        return cell;
    }

    private static String formatarMoeda(BigDecimal valor) {
        if (valor == null) return "R$ 0,00";
        return "R$ " + valor.setScale(2, java.math.RoundingMode.HALF_UP).toString().replace('.', ',');
    }
}