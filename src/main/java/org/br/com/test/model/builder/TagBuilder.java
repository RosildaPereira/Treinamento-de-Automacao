package org.br.com.test.model.builder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TagBuilder {

	private String abaAnalista;
	private String execution;
}
