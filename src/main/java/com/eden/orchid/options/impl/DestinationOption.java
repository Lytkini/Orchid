package com.eden.orchid.options.impl;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.AutoRegister;
import com.eden.orchid.JSONElement;
import com.eden.orchid.options.Option;

@AutoRegister
public class DestinationOption implements Option {

    @Override
    public String getFlag() {
        return "d";
    }

    @Override
    public JSONElement parseOption(String[] options) {
        if(options.length == 2) {
            return new JSONElement(options[1]);
        }
        else {
            Clog.e("'-d' option should be of length 2: given #{$1}", new Object[] {options});
        }

        return null;
    }

    @Override
    public JSONElement getDefaultValue() {
        return null;
    }

    @Override
    public int priority() {
        return 100;
    }
}
