package com.tadpole.poem.service.impl;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.tadpole.poem.domain.Job;
import com.tadpole.poem.service.TagService;
import com.tadpole.poem.domain.Tag;
import com.tadpole.poem.repository.TagRepository;
import com.tadpole.poem.service.util.MathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * Service Implementation for managing Tag.
 */
@Service
@Transactional
public class TagServiceImpl implements TagService{

    private final Logger log = LoggerFactory.getLogger(TagServiceImpl.class);

    @Inject
    private TagRepository tagRepository;

    /**
     * Save a tag.
     *
     * @param tag the entity to save
     * @return the persisted entity
     */
    public Tag save(Tag tag) {
        log.debug("Request to save Tag : {}", tag);
        Tag result = tagRepository.save(tag);
        return result;
    }

    /**
     *  Get all the tags.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Tag> findAll(Pageable pageable) {
        log.debug("Request to get all Tags");
        Page<Tag> result = tagRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one tag by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Tag findOne(Long id) {
        log.debug("Request to get Tag : {}", id);
        Tag tag = tagRepository.findOne(id);
        return tag;
    }

    /**
     *  Delete the  tag by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Tag : {}", id);
        tagRepository.delete(id);
    }

    @Override
    public void grabAllTags(Job job, WebClient webClient) {

        String target = job.getTarget();
        HtmlPage htmlPage = null;
        try {
            htmlPage = webClient.getPage(target);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<HtmlAnchor> tagLinks = (List<HtmlAnchor>) htmlPage.getByXPath("//*[@id=\"tags\"]//a");

        for (HtmlAnchor htmlAnchor: tagLinks) {

            Tag tag = tagRepository.findByIdentifier(htmlAnchor.getTextContent());
            if (tag == null) {
                tag = new Tag();

                tag.setIdentifier(htmlAnchor.getTextContent());
                tag.setLink(htmlAnchor.getHrefAttribute());
                tag.setFontSize(MathUtil.getNumber(htmlAnchor.getAttribute("style")));

                System.err.println(tag.toString());
            }

            tagRepository.save(tag);

        }
    }


}
