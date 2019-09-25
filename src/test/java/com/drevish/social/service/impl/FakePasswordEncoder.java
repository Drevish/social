package com.drevish.social.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;

public class FakePasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return "encoded" + charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return encode(charSequence).equals(s);
    }
}
