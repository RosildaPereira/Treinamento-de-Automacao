package org.br.com.test.manager;

import org.br.com.test.model.request.UsuarioRequest;

public class UsuarioManager {

	private static ThreadLocal<String> emailUsuario = new ThreadLocal<String>();

	private static ThreadLocal<String> senhaUsuario = new ThreadLocal<String>();

	private static ThreadLocal<String> idUsuario = new ThreadLocal<String>();

	private static ThreadLocal<String> nomeCompletoUsuario = new ThreadLocal<String>();

	private static ThreadLocal<String> nomeUsuario = new ThreadLocal<String>();

	public static String getEmailUsuario(){
		return emailUsuario.get();
	}

	public static void setEmailUsuario(String tk){
		emailUsuario.set(tk);
	}

	public static String getSenhaUsuario(){
		return senhaUsuario.get();
	}

	public static void setSenhaUsuario(String tk){
		senhaUsuario.set(tk);
	}

	public static String getIdUsuario(){
		return idUsuario.get();
	}

	public static void setIdUsuario(String tk){
		idUsuario.set(tk);
	}

	/**
	 * Retorna o nome completo do usuário cadastrado/logado no cenário atual.
	 * @return nome completo do usuário
	 */
	public static String getNomeCompletoUsuario(){
		return nomeCompletoUsuario.get();
	}

	public static void setNomeCompletoUsuario(String nome){
		nomeCompletoUsuario.set(nome);
	}

	public static String getNomeUsuario(){
		return nomeUsuario.get();
	}

	public static void setNomeUsuario(String nome){
		nomeUsuario.set(nome);
	}

	public static void remove(){
		emailUsuario.remove();
		senhaUsuario.remove();
		idUsuario.remove();
		nomeCompletoUsuario.remove();
		nomeUsuario.remove();
	}

    /**
     * Retorna um UsuarioRequest com os dados atualmente armazenados.
     */
    public static UsuarioRequest getUsuarioAtual() {
        return UsuarioRequest.builder()
            .email(getEmailUsuario())
            .senha(getSenhaUsuario())
            .nomeCompleto(getNomeCompletoUsuario())
            .nomeUsuario(getNomeUsuario())
            .build();
    }

}

