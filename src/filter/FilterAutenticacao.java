package filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.Scanner;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;



import connection.SingleConnectionBanco;
import dao.DaoVersionadorBanco;


@WebFilter(urlPatterns = {"/principal/*"}) // intercepta todas as requisicoes que vierem do projeto
public class FilterAutenticacao implements Filter {
	
	private static Connection connection;
	
    
    public FilterAutenticacao() {        
    }
	/* encerra os processos quando o servidor é parado*/
    /* mataria os processos de conexao ao banco de dados*/
	public void destroy() {
		
		try {
			connection.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
	}
	/* intercepta as requisicoes e as resposta do sistema*/
	/* tudo que fizer no sistema fará po aqui*/
	/* validacao de autenticacao*/
	/* dar commit e rolback de transacoes do banco*/
	/* validar e fazer 	redirecionamento de  paginas*/
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
		
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();
			
			String usuarioLogrado = (String) session.getAttribute("usuario");
			
			String urlParaAutenticar = req.getServletPath(); // url está sendo acessada
			
			/* validar se está logrado senao redireciona para a tela de login*/
			if( usuarioLogrado == null  &&
					!urlParaAutenticar.equalsIgnoreCase("/principal/ServletLogin")) { // nao está logrado
				// && !urlParaAutenticar.equalsIgnoreCase("/ServletLogin")
				// || (usuarioLogrado != null && usuarioLogrado.isEmpty())
				
				RequestDispatcher redireciona = request.getRequestDispatcher("/index.jsp?url=" + urlParaAutenticar);
				request.setAttribute("msg","Por favor, realize o login");
				redireciona.forward(request, response);
				return; // executar e redirecionar para o login
				
			}else {
				chain.doFilter(request, response);
			}
			
			connection.commit(); // Deu tudo certo, salve as alteracoes no banco de dados
		
		}catch(Exception e){
			e.printStackTrace();
			// para mensagem de erro
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
			
			try {
				connection.rollback();
			} catch (SQLException e1) {				
				e1.printStackTrace();
			}
		}
		
	}
	/* inicia os processos ou recursos quando o servidor  sobe o projeto*/
	/* inicia a conexao com o banco de dados*/
	public void init(FilterConfig fConfig) throws ServletException {
		
		connection = SingleConnectionBanco.getConnection();
		
		DaoVersionadorBanco daoVersionadorBanco = new DaoVersionadorBanco();
		
		String caminhoPasta = fConfig.getServletContext().getRealPath("versionadorbancosql") + File.separator;
		
		File[] filesSql = new File(caminhoPasta).listFiles();
		
		try {
		
			for (File file : filesSql) {
				boolean arquivoRodado = daoVersionadorBanco.arquivoSqlRodado(file.getName());
				
				if(!arquivoRodado) {
					FileInputStream entradaArquivo = new FileInputStream(file);
					
					Scanner lerArquivo = new Scanner(entradaArquivo, "UTF-8");
					
					StringBuilder sql = new StringBuilder();
					
					while (lerArquivo.hasNext()) {
						sql.append(lerArquivo.nextLine());
						sql.append("\n");
					}
					
					connection.prepareStatement(sql.toString()).execute();
					daoVersionadorBanco.gravaArquivoSqlRodado(file.getName());
					
					connection.commit();
					lerArquivo.close();
				}
			}
			
		}catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {				
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
	}
}
