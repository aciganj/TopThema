package hr.math.android.topthema.DAO;

/**
 * Created by kosani on 1/6/15.
 */
public class DAOProvider {
    private DAOProvider(){}
    public static DAO getDAO() {
        return new ActiveAndroidDAO();
    }
}
