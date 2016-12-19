#version 330

in  vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;

void main()
{
    fragColor = texture(texture_sampler, outTexCoord)/* + vec4(0.25f, 0f, 0f, 1f))/2.0f*/;
}