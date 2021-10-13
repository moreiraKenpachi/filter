package connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class SingleConnectionBanco {
	
	private static String banco = "jdbc:postgresql://ec2-35-171-171-27.compute-1.amazonaws.com:5432/d80iud5nkmld1d?autoReconnect=true";
	private static String user = "hkajjyphjbtpta";
	private static String senha = "1af64069742235edf2808790be9ec75e5c70aaf535ab244b8a9f3be4705dce9f";
	private static Connection connection = null;
	
	public static Connection getConnection() {
		return connection;
	}
	
	
	/* pra garantir conexao*/
	static {
		conectar();
	}
	
	/* pra garantir conexao*/
	public SingleConnectionBanco() { // quando tiver uma instancia vai conectar 
		conectar();
	}
	
	private static void conectar() {
		try {
			
			if(connection == null) {
				Class.forName("org.postgresql.Driver");  // carregar o drive de conexao do banco
				connection = DriverManager.getConnection(banco, user, senha);
				connection.setAutoCommit(false); //nao efetuar alteracoes no banco sem nosso comando
			}
		}catch(Exception e){
			e.printStackTrace(); // mostrar qualquer erro no momento de conexao
		}
	}

}
