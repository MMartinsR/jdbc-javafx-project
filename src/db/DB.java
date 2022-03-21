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
	
	// M�todos est�ticos para conex�o e desconex�o do banco
	
	/** Connection conn()
	 * Este objeto ir� ser respons�vel por receber a conex�o com o banco de dados. (usando a classe Connection do pacote sql do
	 * jdbc). 
	 */
	private static Connection conn = null;
	
	
	/** Connection getConnection()
	 * Este m�todo ser� respons�vel por fazer efetivamente a conex�o com o banco de dados.
	 * 
	 * Primeiro, ele carrega as propriedades usando o m�todo loadproperties
	 * 
	 * Em seguida, ele carrega em uma variavel a url do banco que foi definida no arquivo db.properties
	 * 
	 * Para fazer de fato a conex�o, devemos chamar o DriverManager que � uma classe do jdbc, passar a url do banco e as 
	 * propriedades e atribuir a vari�vel conn que ser� a respons�vel por receber a conex�o.
	 * 
	 * Ao fim, essa conex�o estar� salva, toda vez que o m�todo getConnection for chamado depois dessa primeira vez, ele apenas
	 * retornar� a conex�o ao inv�s de tentar se conectar ao banco novamente.
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
	 * Este m�todo ser� respons�vel por fechar a conex�o com o banco.
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
	 * Este m�todo vai acessar o arquivo db.properties, vai carregar os dados de conex�o do banco e vai retornar um
	 * objeto to tipo properties (classe da biblioteca util do Java).
	 * 
	 * Como o arquivo est� na pasta raiz, basta colocar o nome dele	.
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
	
	// M�todos auxiliares para fechar resultSet e Statement
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
