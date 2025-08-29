package org.br.com.core.processor;


import org.br.com.test.model.builder.TagBuilder;
import org.br.com.test.utils.TagConcatenada;

public class TagProcessor {
	public void start(TagBuilder tag){
		TagConcatenada tagConcatenada = new TagConcatenada();
		tagConcatenada.tagsExcel(tag.getAbaAnalista(), tag.getExecution());
	}
}
