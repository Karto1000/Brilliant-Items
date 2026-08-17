#version 120

uniform sampler2D u_texture;
uniform vec2 u_absoluteItemPosition;
uniform vec2 u_scaledScreenSize;
uniform vec4 u_color;
uniform float u_sigma;
uniform int u_radius;

#define PI 3.14159265359

void main() {
    vec2 texelSize = 1. / u_scaledScreenSize;
    vec4 accumulatedColor = vec4(0.);
    float totalWeight = 0.;

    for (int x = -u_radius; x <= u_radius; x++) {
        for (int y = -u_radius; y <= u_radius; y++) {
            float weight = exp(-((pow(x, 2) + pow(y, 2)) / (2 * pow(u_sigma, 2))));
            vec4 texColor = texture2D(u_texture, gl_TexCoord[0].st + (vec2(x, y)) * texelSize);

            accumulatedColor += texColor * weight;
            totalWeight += weight;
        }
    }

    gl_FragColor = (accumulatedColor / totalWeight) * u_color;
}