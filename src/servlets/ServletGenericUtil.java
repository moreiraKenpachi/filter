package servlets;

import java.io.Serializable;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import dao.DAOUsuarioRepository;
import model.ModelLogin;

public class ServletGenericUtil extends HttpServlet implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();
	
				/** Metodo Retorna ficar sabendo se o usuario está logrado*/
	public Long getUserLogrado(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		
		String usuarioLogrado = (String) session.getAttribute("usuario");
		return daoUsuarioRepository.consultaUsuarioLogrado(usuarioLogrado).getId();
	}
	
	public ModelLogin getUserLogradoObjct(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		
		String usuarioLogrado = (String) session.getAttribute("usuario");
		return daoUsuarioRepository.consultaUsuarioLogrado(usuarioLogrado);
	}

}
