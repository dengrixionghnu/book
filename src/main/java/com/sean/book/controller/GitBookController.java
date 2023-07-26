package com.sean.book.controller;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.vladsch.flexmark.ext.abbreviation.AbbreviationExtension;
import com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.definition.DefinitionExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.escaped.character.EscapedCharacterExtension;
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughSubscriptExtension;
import com.vladsch.flexmark.ext.jekyll.front.matter.JekyllFrontMatterExtension;
import com.vladsch.flexmark.ext.gfm.issues.GfmIssuesExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.macros.MacrosExtension;
import com.vladsch.flexmark.ext.media.tags.MediaTagsExtension;
import com.vladsch.flexmark.ext.typographic.TypographicExtension;
import com.vladsch.flexmark.ext.xwiki.macros.MacroExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class GitBookController {
    private final Handlebars handlebars;
    private final TemplateLoader templateLoader;
    private final Parser markdownParser;

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    public GitBookController() throws IOException {
        this.templateLoader = new FileTemplateLoader("./templates", ".html");
        this.handlebars = new Handlebars(templateLoader);
        this.handlebars.setPrettyPrint(true);

        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, java.util.Arrays.asList(
                AbbreviationExtension.create(),
                AnchorLinkExtension.create(),
                AutolinkExtension.create(),
                DefinitionExtension.create(),
                EmojiExtension.create(),
                EscapedCharacterExtension.create(),
                FootnoteExtension.create(),
                GfmIssuesExtension.create(),
                JekyllFrontMatterExtension.create(),
                MacrosExtension.create(),
                MacroExtension.create(),
                MediaTagsExtension.create(),
                StrikethroughSubscriptExtension.create(),
                TaskListExtension.create(),
                TypographicExtension.create()
        ));

        this.markdownParser = Parser.builder(options).build();
    }

    @GetMapping("/books/{pageName}")
    public String renderPage(@PathVariable String pageName, Model model) throws IOException {
        String markdownContent = loadMarkdownContent(pageName);
        String htmlContent = renderMarkdown(markdownContent);
        model.addAttribute("htmlContent", htmlContent);
        return "book";
    }

    private String loadMarkdownContent(String pageName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + "/templates/books/" + pageName + ".md");
        return IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    private String renderMarkdown(String markdownContent) {
        return HtmlRenderer.builder()
                .extensions(java.util.Arrays.asList(
                        AbbreviationExtension.create(),
                        AnchorLinkExtension.create(),
                        AutolinkExtension.create(),
                        DefinitionExtension.create(),
                        EmojiExtension.create(),
                        EscapedCharacterExtension.create(),
                        FootnoteExtension.create(),
                        GfmIssuesExtension.create(),
                        JekyllFrontMatterExtension.create(),
                        MacrosExtension.create(),
                        MacroExtension.create(),
                        MediaTagsExtension.create(),
                        StrikethroughSubscriptExtension.create(),
                        TaskListExtension.create(),
                        TypographicExtension.create()
                ))
                .build()
                .render(markdownParser.parse(markdownContent));
    }
}