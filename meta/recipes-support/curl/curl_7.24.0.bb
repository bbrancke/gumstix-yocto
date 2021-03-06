DESCRIPTION = "Command line tool and library for client-side URL transfers."
HOMEPAGE = "http://curl.haxx.se/"
BUGTRACKER = "http://curl.haxx.se/mail/list.cgi?list=curl-tracker"
SECTION = "console/network"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;beginline=7;md5=3a34942f4ae3fbf1a303160714e664ac"

DEPENDS = "zlib gnutls"
DEPENDS_virtclass-native = "zlib-native openssl-native"
DEPENDS_virtclass-nativesdk = "zlib-nativesdk"
PR = "r2"

SRC_URI = "http://curl.haxx.se/download/curl-${PV}.tar.bz2 \
           file://noldlibpath.patch \
           file://pkgconfig_fix.patch"

SRC_URI[md5sum] = "f912221d75eb8d8fe08900eaf011b023"
SRC_URI[sha256sum] = "ebdb111088ff8b0e05b1d1b075e9f1608285e8105cc51e21caacf33d01812c16"

inherit autotools pkgconfig binconfig

EXTRA_OECONF = "--with-zlib=${STAGING_LIBDIR}/../ \
                --without-libssh2 \
		--with-random=/dev/urandom \
		--without-libidn \
		--enable-crypto-auth \
                ${CURLGNUTLS} \
		"

CURLGNUTLS = " --with-gnutls=${STAGING_LIBDIR}/../ --without-ssl"
CURLGNUTLS_virtclass-native = "--without-gnutls --with-ssl"
CURLGNUTLS_virtclass-nativesdk = "--without-gnutls --without-ssl"

do_configure_prepend() {
	sed -i s:OPT_GNUTLS/bin:OPT_GNUTLS:g configure.ac
}

PACKAGES += "${PN}-certs libcurl libcurl-dev libcurl-doc"

FILES_${PN} = "${bindir}/curl"

FILES_${PN}-certs = "${datadir}/curl/curl-*"
PACKAGE_ARCH_${PN}-certs = "all"

FILES_${PN}-doc = "${mandir}/man1/curl.1"

FILES_lib${BPN} = "${libdir}/lib*.so.*"
RRECOMMENDS_lib${BPN} += "${PN}-certs"
FILES_lib${BPN}-dev = "${includedir} \
                      ${libdir}/lib*.so \
                      ${libdir}/lib*.a \
                      ${libdir}/lib*.la \
                      ${libdir}/pkgconfig \
                      ${datadir}/aclocal \
                      ${bindir}/*-config"

FILES_lib${BPN}-doc = "${mandir}/man3 \
                      ${mandir}/man1/curl-config.1"

BBCLASSEXTEND = "native nativesdk"
