#version 330

in  vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform float stepSize = 0.001625;
uniform float fillFactor = 0.4f;
uniform vec3 glowColor = vec3(217, 97, 168)/256.0;

void main()
{
    vec4 texColor = texture(texture_sampler, outTexCoord);

    float hDelta = abs(
        texture(texture_sampler, outTexCoord + vec2(stepSize, 0)).a -
        texture(texture_sampler, outTexCoord + vec2(-stepSize, 0)).a  );

    float vDelta = abs(
        texture(texture_sampler, outTexCoord + vec2(0, stepSize)).a -
        texture(texture_sampler, outTexCoord + vec2(0, -stepSize)).a  );

    float aDelta = max(hDelta, vDelta);
    fragColor = vec4(glowColor, aDelta)*aDelta +
        (1.0-aDelta)*vec4((1-fillFactor)*texColor.rgb + fillFactor*glowColor.rgb, texColor.a );

}