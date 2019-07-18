package helper;

import java.sql.*;

public class ConnectDbUtil {

    /**
     * @Name: getDbConn
     * @Description: 建立数据库连接，返回连接对象
     */

    public static Connection getDbConn(String username, String password, String ip, String dbname) {
        String driverName = "driverName";
        String dburl = "jdbc:oracle:thin:@" + ip + ":1521:" + dbname;
        Connection conn = null;
        try {
            Class.forName(driverName);
            conn = (Connection) DriverManager.getConnection(dburl, username, password);
            System.out.println("------数据库连接成功-----");
        } catch (ClassNotFoundException e) {
            System.out.println("------加载驱动失败，驱动类未找到------");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("------数据库连接失败------");
        }

        return conn;
    }

    /**
     * @Name: close
     * @Description: 关闭数据库连接
     */
    public static void close(ResultSet result, PreparedStatement pstmt, Connection conn) {
        try {
            if (result != null) {
                result.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("------数据库连接已关闭！------");
    }
}
