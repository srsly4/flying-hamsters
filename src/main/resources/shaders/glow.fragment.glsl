#version 330

in  vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform float rnd;
uniform float intensity;

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 4.5453);
}

void main (void)
{
	float f = rand(vec2(int(gl_FragCoord.y/3), int(gl_FragCoord.x/3)+rnd))*rand(vec2(int(gl_FragCoord.y/2), int(gl_FragCoord.x/2)+rnd))*rand(vec2(int(gl_FragCoord.y/2), int(gl_FragCoord.x/2)+rnd));

	float c1 = texture(texture_sampler, outTexCoord).x + texture(texture_sampler, outTexCoord).y + texture(texture_sampler, outTexCoord).z;
	c1 *= 3;

	vec4 color = vec4((1.0f - min(c1, 1.0f)) * f, (1.0f - min(c1, 1.0f)) * f, 0, 0);

	fragColor = texture(texture_sampler, outTexCoord) + texture(texture_sampler, outTexCoord) * vec4(color.xyz, 1.0) + color*intensity;
}