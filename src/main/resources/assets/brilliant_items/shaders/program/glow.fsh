#version 120

uniform sampler2D u_texture;
uniform vec2 u_absoluteItemPosition;
uniform vec2 u_scaledScreenSize;
uniform vec4 u_color;

void main() {
    vec4 texColor = texture2D(u_texture, gl_TexCoord[0].st);

    if (texColor.a == 0) {
        vec2 pixelPos = vec2(gl_TexCoord[0].s, 1.0 - gl_TexCoord[0].t) * u_scaledScreenSize;
        vec2 center = u_absoluteItemPosition + vec2(8.);

        vec2 difference = (pixelPos - center) / vec2(12);
        float dist = length(difference);
        float alpha = clamp(1.0 - dist, 0.0, 1.0);

        if (alpha > 0.0) {
            gl_FragColor = vec4(u_color.rgb, clamp(u_color.a * alpha, 0, 0.8));
            return;
        }
    }

    gl_FragColor = vec4(0., 0., 0., 0.);
}