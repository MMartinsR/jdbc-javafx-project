module aula_58jdbcjavafxProject {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.graphics;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
	opens gui to javafx.fxml;
	opens model.entities to javafx.base;
}
