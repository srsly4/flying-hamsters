#version 330

in  vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec4 faceColor = vec4(1, 1, 1, 1);

void main()
{
    vec4 texColor = texture(texture_sampler, outTexCoord);
    fragColor = texColor * faceColor;
}