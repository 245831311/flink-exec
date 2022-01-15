package table.api;

import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.types.Row;

/**
 * 使用简单用法
 * @author Lenovo
 *
 */
public class TestApi1 {

	public static void main(String[] args) {
		EnvironmentSettings settings = EnvironmentSettings.newInstance().inBatchMode().build();
		TableEnvironment tableEnv = TableEnvironment.create(settings);
		
		Table table =  tableEnv.fromValues(
				DataTypes.ROW(
				        DataTypes.FIELD("id", DataTypes.DECIMAL(10, 2)),
				        DataTypes.FIELD("name", DataTypes.STRING())
				    ),
				Row.of(1L,"ABC"),
				Row.of(2L,"ABCD"),
				Row.of(3L,"ABC")
		);
		//tableEnv.registerTable("WordCount",table);
		tableEnv.createTemporaryView("wc",table);
		Table count = tableEnv.from("wc")
				.groupBy("name").select("name,id.sum as count");
		
		Table sqlCount = tableEnv.sqlQuery("select sum(id) as c from wc group by(name) ");
		
		
		sqlCount.execute().print();
	}
	
	
}
