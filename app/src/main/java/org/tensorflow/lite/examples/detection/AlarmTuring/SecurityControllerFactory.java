package org.tensorflow.lite.examples.detection.AlarmTuring;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.SecurityLevel;
import org.tensorflow.lite.examples.detection.AlarmTuring.SecurityLevelsUtils.SecurityLevelFactory;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SecurityControllerFactory {

    private static final List<SecurityLevel> securityLevelList = SecurityLevelFactory.createSecurityLevelList();

    public static SecurityController createSecurityController(int securityLevelNum, boolean enabled) {
        SecurityLevel securityLevel = securityLevelList.get(securityLevelNum);
        SecurityController s = new SecurityController(securityLevel);
        s.setActivated(enabled);
        return s;
    }

    public static SecurityController createSecurityController(SecurityLevel securityLevel, boolean enabled) {
        SecurityController s = new SecurityController(securityLevel);
        s.setActivated(enabled);
        return s;
    }

    public static List<SecurityLevel> getSecurityLevelList() {
        return securityLevelList;
    }
}
