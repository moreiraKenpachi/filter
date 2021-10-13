package servlets;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;


import com.fasterxml.jackson.databind.ObjectMapper;

import beandto.BeanDtoGraficoSalarialUser;
import dao.DAOUsuarioRepository;
import model.ModelLogin;
import util.ReportUtil;

@MultipartConfig
@WebServlet(urlPatterns = {"/ServletUsuarioController"} )
public class ServletUsuarioController extends ServletGenericUtil {
	private static final long serialVersionUID = 1L;
	
	private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();
    
    public ServletUsuarioController() {
                
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {		
		try {
		
		String acao = request.getParameter("acao");
		
		if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletar")) {
			
			String idUser = request.getParameter("id");
			daoUsuarioRepository.deletarUser(idUser);
			
			List<ModelLogin> listaLogin = daoUsuarioRepository.listaUsuario(super.getUserLogrado(request));			
			request.setAttribute("listaLogin", listaLogin);
			
			// ******request.setAttribute("msg", "Excluido com sucesso!");
			response.getWriter().write("Excluido com sucesso!");
			request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogrado(request)));
			
			RequestDispatcher redireciona = request.getRequestDispatcher("principal/usuario.jsp");
			redireciona.forward(request, response);
			
		}else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletarajax")) {
				
				String idUser = request.getParameter("id");
				daoUsuarioRepository.deletarUser(idUser);				
				
				response.getWriter().write("Excluido com sucesso!");
		
		}else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarUserajax")) {
			
			String nomeUsuario = request.getParameter("nomeUsuario");
			// System.out.println(nomeUsuario);
			
			List<ModelLogin> dadosJsonUser =  daoUsuarioRepository.consultaUsuarioList(nomeUsuario, super.getUserLogrado(request));			
			
			ObjectMapper mapper = new ObjectMapper();
			
			String jason = mapper.writeValueAsString(dadosJsonUser);
			
			response.addHeader("totalPagina","" + daoUsuarioRepository.consultaUsuarioListPesquisaPaginada(nomeUsuario, super.getUserLogrado(request)));
			response.getWriter().write(jason);
			
	
		}else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarUserajaxPagination")) {
			
			String nomeUsuario = request.getParameter("nomeUsuario");
			String pagina = request.getParameter("pagina");
			// System.out.println(nomeUsuario);
			
			List<ModelLogin> dadosJsonUser =  daoUsuarioRepository.consultaUsuarioListOffSet(nomeUsuario, super.getUserLogrado(request), Integer.parseInt(pagina));			
			
			ObjectMapper mapper = new ObjectMapper();
			
			String jason = mapper.writeValueAsString(dadosJsonUser);
			
			response.addHeader("totalPagina","" + daoUsuarioRepository.consultaUsuarioListPesquisaPaginada(nomeUsuario, super.getUserLogrado(request)));
			response.getWriter().write(jason);
			
	
		}else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarEditar")) {
			String id = request.getParameter("id");
			
			ModelLogin modelLogin = daoUsuarioRepository.consultaUsuarioId(id, super.getUserLogrado(request));
			
			List<ModelLogin> listaLogin = daoUsuarioRepository.listaUsuario(super.getUserLogrado(request));			
			request.setAttribute("listaLogin", listaLogin);
			
			request.setAttribute("msg", "Usuário em Edição");
			request.setAttribute("modeloLogin", modelLogin);
			
			request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogrado(request)));
			RequestDispatcher redireciona = request.getRequestDispatcher("principal/usuario.jsp");
			redireciona.forward(request, response);
			
		}else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("listarUser")) {
			
			List<ModelLogin> listaLogin = daoUsuarioRepository.listaUsuario(super.getUserLogrado(request));
			
			request.setAttribute("msg", "Usuários carregados");
			request.setAttribute("listaLogin", listaLogin);
			
			request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogrado(request)));
			RequestDispatcher redireciona = request.getRequestDispatcher("principal/usuario.jsp");
			redireciona.forward(request, response);
			
		}else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("downloadFoto")) {
			String idUser = request.getParameter("id");
			
			ModelLogin modelLogin = daoUsuarioRepository.consultaUsuarioId(idUser, super.getUserLogrado(request));
			if(modelLogin.getFotouser() != null && !modelLogin.getFotouser().isEmpty()) {
				
				response.setHeader("Content-disposition", "attachment;filename=arquivo." + modelLogin.getExtensaofotouser());
				response.getOutputStream().write(new Base64().decodeBase64(modelLogin.getFotouser().split("\\,")[1]));
				
			}
			
		}else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("paginacao")) {
					
			Integer offset = Integer.parseInt(request.getParameter("paginar"));
			
			List<ModelLogin> listaLogin = daoUsuarioRepository.paginacaolistaUsuario(this.getUserLogrado(request), offset);
			
			request.setAttribute("listaLogin", listaLogin);			
			request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogrado(request)));
			request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			
		}else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("imprimirRelatorioUser")) {
			
			String dataInicial = request.getParameter("dataInicial");
			String dataFinal = request.getParameter("dataFinal");
			
			if(dataInicial == null || dataInicial.isEmpty() &&
					dataFinal == null || dataFinal.isEmpty()) {
				
				request.setAttribute("listaUser", daoUsuarioRepository.listaUsuarioRelatorio(super.getUserLogrado(request)));
				
			}else {
				
				request.setAttribute("listaUser", daoUsuarioRepository
						.listaUsuarioRelatorio(super.getUserLogrado(request), dataInicial, dataFinal));
			}
			
			request.setAttribute("dataInicial", dataInicial);
			request.setAttribute("dataFinal", dataFinal);
			request.getRequestDispatcher("principal/reluser.jsp").forward(request, response);
		}
		
		else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("imprimirRelatorioPDF")) {
			
			String dataInicial = request.getParameter("dataInicial");
			String dataFinal = request.getParameter("dataFinal");
			
			List<ModelLogin> modelLogins = null;
			
			if(dataInicial == null || dataInicial.isEmpty() &&
					dataFinal == null || dataFinal.isEmpty()) {
				
				modelLogins = daoUsuarioRepository.listaUsuarioRelatorio(super.getUserLogrado(request));
				
			}else {
				modelLogins = daoUsuarioRepository
						.listaUsuarioRelatorio(super.getUserLogrado(request), dataInicial, dataFinal);				
			}
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("PARAM_SUB_REPORT", request.getServletContext().getRealPath("relatorio") + File.separator);
			
			byte[] relatorio = new ReportUtil().geraRelatorioPDF(modelLogins, "rel-user-jsp", params, request.getServletContext());
			
			response.setHeader("Content-disposition", "attachment;filename=arquivo.pdf");
			response.getOutputStream().write(relatorio);
			
		}
		
		else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("graficoSalario")) {
			
			String dataInicial = request.getParameter("dataInicial");
			String dataFinal = request.getParameter("dataFinal");
			
			if(dataInicial == null || dataInicial.isEmpty() &&
					dataFinal == null || dataFinal.isEmpty()) {
				
				BeanDtoGraficoSalarialUser beanDtoGraficoSalarialUser = daoUsuarioRepository
						.montarGraficoMediaSalarial(super.getUserLogrado(request));
				
				ObjectMapper mapper = new ObjectMapper();
				
				String jason = mapper.writeValueAsString(beanDtoGraficoSalarialUser);
				
				response.getWriter().write(jason);
				
			}else {
				
				BeanDtoGraficoSalarialUser beanDtoGraficoSalarialUser = daoUsuarioRepository
						.montarGraficoMediaSalarial(super.getUserLogrado(request), dataInicial, dataFinal);
				
				ObjectMapper mapper = new ObjectMapper();
				
				String jason = mapper.writeValueAsString(beanDtoGraficoSalarialUser);
				
				response.getWriter().write(jason);
				
			}
			
			
			
		}
		
		else {
			
			List<ModelLogin> listaLogin = daoUsuarioRepository.listaUsuario(super.getUserLogrado(request));			
			request.setAttribute("listaLogin", listaLogin);
			
			request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogrado(request)));
			request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
		}
		
		}catch (Exception e) {
			e.printStackTrace();
			// para mensagem de erro
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		try {
		
		String msg = "Operação realizada com sucesso!!!";
			
		/* pega valores do formulario*/
		String id = request.getParameter("id");
		String nome = request.getParameter("nome");
		String email = request.getParameter("email");
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		String perfil = request.getParameter("perfil");
		String sexo = request.getParameter("sexo");
		
		String cep = request.getParameter("cep");
		String logradouro = request.getParameter("logradouro");
		String bairro = request.getParameter("bairro");
		String localidade = request.getParameter("localidade");
		String uf = request.getParameter("uf");
		String numero = request.getParameter("numero");
		String dataNascimento = request.getParameter("dataNascimento");
		String rendaMensal = request.getParameter("rendamensal");
		
		rendaMensal = rendaMensal.split("\\ ")[1].replaceAll("\\.", "").replaceAll("\\,", ".");
		// rendaMensal = rendaMensal.split("\\ ")[1].replaceAll("\\.", "");
		
		ModelLogin modelLogin = new ModelLogin();
		
		modelLogin.setId(id != null && !id.isEmpty() ? Long.parseLong(id) : null);
		modelLogin.setNome(nome);
		modelLogin.setEmail(email);
		modelLogin.setLogin(login);
		modelLogin.setSenha(senha);
		modelLogin.setPerfil(perfil);
		modelLogin.setSexo(sexo);
		modelLogin.setCep(cep);
		modelLogin.setLogradouro(logradouro);
		modelLogin.setBairro(bairro);
		modelLogin.setLocalidade(localidade);
		modelLogin.setUf(uf);
		modelLogin.setNumero(numero);		
		modelLogin.setDataNascimento(Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataNascimento))));
		modelLogin.setRendamensal(Double.valueOf(rendaMensal));
		
		if(ServletFileUpload.isMultipartContent(request)) {
			
			Part part = request.getPart("fileFoto"); // pega foto da tela 
			
			if(part.getSize() > 0) {
				byte[] foto = IOUtils.toByteArray(part.getInputStream()); // converte imagem para byte
				String imagemBase64 = "data:image/" + part.getContentType().split("\\/")[1] + ";base64," + new Base64().encodeBase64String(foto);
				
				modelLogin.setFotouser(imagemBase64);
				modelLogin.setExtensaofotouser(part.getContentType().split("\\/")[1]);
			}
		}
		
		if(daoUsuarioRepository.validarLogin(modelLogin.getLogin()) && modelLogin.getId() == null) {
			msg = "Já existe esse login, por favor informe um novo login!";
		}else {
			if(modelLogin.isNovo()) {
				msg = "Gravado com sucesso!";
			}else {
				msg = "Atualizado com sucesso!";
			}
			modelLogin = daoUsuarioRepository.gravarUsuario(modelLogin, super.getUserLogrado(request));
		}
		
		List<ModelLogin> listaLogin = daoUsuarioRepository.listaUsuario(super.getUserLogrado(request));		
		request.setAttribute("listaLogin", listaLogin);
				
		request.setAttribute("msg", msg);
		request.setAttribute("modeloLogin", modelLogin);
		
		request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogrado(request)));
		RequestDispatcher redireciona = request.getRequestDispatcher("principal/usuario.jsp");
		redireciona.forward(request, response);
		
		}catch (Exception e) {
			e.printStackTrace();
			// para mensagem de erro
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}		
	}
}
