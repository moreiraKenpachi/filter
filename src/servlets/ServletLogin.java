package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOLoginRepository;
import dao.DAOUsuarioRepository;
import model.ModelLogin;

	/****** servlets tambem sao controller *******/
@WebServlet(urlPatterns = {"/principal/ServletLogin", "/ServletLogin"}) // mapeamento de URL que vem da tela principal(index.jsp)
public class ServletLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DAOLoginRepository daoLoginRepository = new DAOLoginRepository();
	private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();
           
    public ServletLogin() {
        super();       
    }
    
    // Recebe dados pela URL em parametros
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String acao = request.getParameter("acao");
		
		if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("logout")) {
			
			request.getSession().invalidate(); // invalida a sessao
			RequestDispatcher redirecionar = request.getRequestDispatcher("index.jsp");
			redirecionar.forward(request, response);
			
		}else {
			doPost(request, response);
		}
				
	}

	// Recebe dados de um formulario (do index.jsp)
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/**os dados enviados para o console*/
		//------ System.out.println(request.getParameter("Login"));
		//------ System.out.println(request.getParameter("senha"));
		
		/* request seria uma ideia de escrever em algum lugar*/
		/* response seria uma ideia de mostrar em algum lugar*/
		/* pego os dados da tela*/
		String login = request.getParameter("Login");
		String senha = request.getParameter("senha");
		String url = request.getParameter("url");
		
		try {
		
			if(login != null && !login.isEmpty() && senha != null && !senha.isEmpty()) {
				
				/* enviado os dados para o objeto */
				ModelLogin modellogin = new ModelLogin();
				modellogin.setLogin(login);
				modellogin.setSenha(senha);
				
				if(daoLoginRepository.validarAutenticacao(modellogin)) { // simulando o login
					/**************modellogin.getLogin().equalsIgnoreCase("admin") &&
					modellogin.getSenha().equalsIgnoreCase("admin")**********************/
					
					modellogin = daoUsuarioRepository.consultaUsuarioLogrado(login);
					
					request.getSession().setAttribute("usuario", modellogin.getLogin());
					request.getSession().setAttribute("perfil", modellogin.getPerfil());
					
					request.getSession().setAttribute("imagemUser", modellogin.getFotouser());
					
					if(url == null || url.equals("null")){
						url = "principal/principal.jsp";
					}
					
					RequestDispatcher redirecionar = request.getRequestDispatcher(url);				
					redirecionar.forward(request, response);
				}else {
					RequestDispatcher redirecionar = request.getRequestDispatcher("/index.jsp");
					request.setAttribute("msg", "informe o login e a senha corretamente!");
					redirecionar.forward(request, response);
				}
				
			}else {
				RequestDispatcher redirecionar = request.getRequestDispatcher("index.jsp");
				request.setAttribute("msg", "informe o login e a senha corretamente!");
				redirecionar.forward(request, response);
			}
		
		}catch(Exception e) {
			e.printStackTrace();
			// para mensagem de erro
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}
		
	}
}
