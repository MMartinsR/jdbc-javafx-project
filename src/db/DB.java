package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
	
	// Métodos estáticos para conexão e desconexão do banco
	
	/** Connection conn()
	 * Este objeto irá ser responsável por receber a conexão com o banco de dados. (usando a classe Connection do pacote sql do
	 * jdbc). 
	 */
	private static Connection conn = null;
	
	
	/** Connection getConnection()
	 * Este método será responsável por fazer efetivamente a conexão com o banco de dados.
	 * 
	 * Primeiro, ele carrega as propriedades usando o método loadproperties
	 * 
	 * Em seguida, ele carrega em uma variavel a url do banco que foi definida no arquivo db.properties
	 * 
	 * Para fazer de fato a conexão, devemos chamar o DriverManager que é uma classe do jdbc, passar a url do banco e as 
	 * propriedades e atribuir a variável conn que será a responsável por receber a conexão.
	 * 
	 * Ao fim, essa conexão estará salva, toda vez que o método getConnection for chamado depois dessa primeira vez, ele apenas
	 * retornará a conexão ao invés de tentar se conectar ao banco novamente.
	 * 
	 * @return conn
	 */
	public static Connection getConnection() {
		if (conn == null) {
			try {
				Properties props = loadProperties();			
				String url = props.getProperty("dburl");
				conn = DriverManager.getConnection(url, props);
				
			} 
			catch (SQLException e) {
				throw new DbException(e.getMessage());
				
			}
		}
		return conn;		
	}
	
	
	/** void closeConnection()
	 * Este método será responsável por fechar a conexão com o banco.
	 */
	public static void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
				
			}
			catch(SQLException e) {
				throw new DbException(e.getMessage());
				
			}
		}		
	}

	
	/** Properties loadProperties()
	 * Este método vai acessar o arquivo db.properties, vai carregar os dados de conexão do banco e vai retornar um
	 * objeto to tipo properties (classe da biblioteca util do Java).
	 * 
	 * Como o arquivo está na pasta raiz, basta colocar o nome dele	.
	 *  	
	 * @return props
	 */
	private static Properties loadProperties() {		
		
		try (FileInputStream fs = new FileInputStream("db.properties")){
			Properties props = new Properties();
			props.load(fs);
			return props;
			
		}catch (IOException e){
			throw new DbException(e.getMessage());
			
		}
	}
	
	// Métodos auxiliares para fechar resultSet e Statement
	public static void closeStatement(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
				
			}
			catch(SQLException e) {
				throw new DbException(e.getMessage());
				
			}
		}		
	}
	
	
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				
			}
			catch(SQLException e) {
				throw new DbException(e.getMessage());
				
			}
		}		
	}
}
